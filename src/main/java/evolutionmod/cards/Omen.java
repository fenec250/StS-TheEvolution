package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.actions.FateAction;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;

public class Omen
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Omen";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ShadowSkl.png";
    private static final int COST = 1;
    private static final int FATE_WEAK_AMT = 4;
    private static final int UPGRADE_FATE_WEAK_AMT = 2;
    private static final int LYMEAN_FATE_AMT = 1;
    private static final int SHADOW_WEAK_AMT = 2;

    public Omen() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = FATE_WEAK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int fate = this.magicNumber;
        if (!AbstractGene.isPlayerInThisForm(LymeanGene.ID)) {
            addToBot(new ChannelAction(new LymeanGene()));
        } else {
            fate += LYMEAN_FATE_AMT;
        }
        if (this.upgraded || !AbstractGene.isPlayerInThisForm(ShadowGene.ID)) {
            addToBot(new ChannelAction(new ShadowGene()));
        } else {
            addToBot(new ApplyPowerAction(m, p,
                    new WeakPower(m, SHADOW_WEAK_AMT, false), SHADOW_WEAK_AMT));
        }
        AbstractDungeon.actionManager.addToBottom(new FateAction(fate, l -> {
            l.forEach(c -> {
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                if (c.type == CardType.CURSE) {
                    addToBot(new ApplyPowerAction(m, p,
                            new WeakPower(m, this.magicNumber, false), this.magicNumber));
                }
            });
        }));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Omen();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FATE_WEAK_AMT);
        }
    }
}