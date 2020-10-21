package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.FateAction;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.patches.AbstractCardEnum;

public class PeaceAndTranquility
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:PeaceAndTranquility";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LymeanSkl.png";
    private static final int COST = 1;
    private static final int FATE_AMT = 3;
    private static final int BLOCK_AMT = 2;
    private static final int UPGRADE_BLOCK_AMT = 2;
    private static final int LYMEAN_FORM_FATE_AMT = 1;

    public PeaceAndTranquility() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = FATE_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int fate = this.magicNumber;
        int block = this.block;
        if (!AbstractGene.isPlayerInThisForm(LymeanGene.ID)) {
            addToBot(new ChannelAction(new LymeanGene()));
        } else {
            fate += LYMEAN_FORM_FATE_AMT;
        }
        if (this.upgraded || !AbstractGene.isPlayerInThisForm(MerfolkGene.ID)) {
            addToBot(new ChannelAction(new MerfolkGene()));
        } else {
            block += UPGRADE_BLOCK_AMT;
        }
        final int finalBlock = block;
        AbstractDungeon.actionManager.addToBottom(new FateAction(fate, l -> {
            l.forEach(c -> {
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                if (c.type == CardType.ATTACK) {
                    addToTop(new GainBlockAction(p, finalBlock));
                }
            });
        }));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PeaceAndTranquility();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(LYMEAN_FORM_FATE_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}