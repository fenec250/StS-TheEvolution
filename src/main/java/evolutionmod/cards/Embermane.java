package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.EmbermanePower;

public class Embermane
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Embermane";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Embermane.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int FORM_CHANNEL_AMT = 1;

    public Embermane() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = FORM_CHANNEL_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new LavafolkGene().getChannelAction());
        if (upgraded) {
            addToBot(new BeastGene().getChannelAction());
            addToBot(new ApplyPowerAction(p, p, new EmbermanePower(p)));
        }
        else
            BaseEvoCard.formEffect(BeastGene.ID, () -> addToBot(new ApplyPowerAction(p, p, new EmbermanePower(p))));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Embermane();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(BeastGene.ID)) {
            this.glowColor = BeastGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}