package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.FateAction;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;

public class Regenerate
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Regenerate";
//    public static final String ID = "evolutionmod:Antidote";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LizardSkl.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 7;
    private static final int UPGRADE_BLOCK_AMT = 2;
    private static final int FATE_AMT = 2;
    private static final int UPGRADE_FATE_AMT = 1;

    public Regenerate() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = FATE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        addToBot(new FateAction(this.magicNumber, l -> {
            l.forEach(c -> {
                if (c.type == CardType.STATUS) {
                    AbstractDungeon.player.drawPile.moveToExhaustPile(c);
                } else {
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                }
            });
        }));
        AbstractCard status = p.hand.getRandomCard(CardType.STATUS, true);
        if (status != null) {
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(status, p.hand));
        }
        if (!AbstractGene.isPlayerInThisForm(LymeanGene.ID)) {
            addToBot(new ChannelAction(new LymeanGene()));
        } else {
            addToBot(new ChannelAction(new LizardGene()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Regenerate();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.upgradeMagicNumber(UPGRADE_FATE_AMT);
        }
    }
}