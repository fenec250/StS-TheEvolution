package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.NirvanaPower;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;

public class Aegis2
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Aegis2";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LymeanPower.png";
    private static final int COST = 1;
    private static final int FATE_BLOCK = 3;
    private static final int UPGRADE_FATE_BLOCK = 1;
    private static final int LYMEAN_BLOCK = 3;
    private static final int UPGRADE_LYMEAN_BLOCK = 1;

    public Aegis2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = FATE_BLOCK;
        this.baseBlock = this.block = LYMEAN_BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new NirvanaPower(p, this.magicNumber), this.magicNumber));
        BaseEvoCard.formEffect(LymeanGene.ID, () -> addToBot(new GainBlockAction(p, this.block)));
    }

//    @Override
//    public void onPlayCard(AbstractCard c, AbstractMonster m) {
//        super.onPlayCard(c, m);
//        if (c.type == CardType.ATTACK) {
//            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
//        }
//    }

    @Override
    public AbstractCard makeCopy() {
        return new Aegis2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FATE_BLOCK);
            this.upgradeBlock(UPGRADE_LYMEAN_BLOCK);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(LymeanGene.ID)) {
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}